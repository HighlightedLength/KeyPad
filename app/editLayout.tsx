import { KeyMap } from "@/assets/keyboardLayouts/types";
import Keyboard from "@/components/Keyboard";
import { KeyboardSelector } from "@/components/KeyboardSelector";
import { KeyRow } from "@/components/KeyRow";
import { KeyboardLayoutSelectors } from "@/data/keyboardLayouts";
import { RootState } from "@/data/store";
import { useEffect, useMemo, useState } from "react";
import { FlatList, TextInput, View } from "react-native";
import { useSelector } from "react-redux";

function EditLayout() {
    const [selectedLayout, setSelectedLayout] = useState<KeyMap | undefined>(undefined);
    const layoutList = useSelector(KeyboardLayoutSelectors.selectLayoutList);
    const keyboardStyle = useSelector((state: RootState) => state.keyboard.style);
    const memoedKeys = useMemo(() => Array.from(selectedLayout?.entries() || []), [selectedLayout]);
    const [selectedLayoutName, setSelectedLayoutName] = useState("");
    useEffect(() => {
        const result = layoutList.find((layout) => layout.id === selectedLayoutName)?.keys;
        console.log("Selected Layout Changed: ", selectedLayoutName, selectedLayout);
        setSelectedLayout(result);
    }, [selectedLayoutName, layoutList]);
    return (
        <View style={{ flexDirection: "column", flex: 1, justifyContent: "space-between", padding: 16, gap: 16 }}>
            <KeyboardSelector selectedValue={selectedLayoutName} onValueChange={(itemValue) => setSelectedLayoutName(itemValue)}/>
            <TextInput
                onChangeText={() => {}}
                value={selectedLayoutName}
            />
            <FlatList
                contentContainerStyle={{ flex: 1 }}
                data={memoedKeys}
                keyExtractor={(item) => item[0]}
                ListFooterComponent={<KeyRow item={["", 0]} iconName="plus" />}
                renderItem={({ item }) => (<KeyRow item={item} iconName="edit" />)}
            />
            <Keyboard layout={selectedLayout} keyboardStyle={[keyboardStyle, { flex: 1 }]} />
        </View>
    );
}

export default EditLayout;
