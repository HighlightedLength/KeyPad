import AntDesign from "@expo/vector-icons/AntDesign";
import { useEffect, useState } from "react";
import { TextInput, View } from "react-native";

function KeyRow(
    { item, iconName, onPress }: 
    { 
        item: [string, number];
        iconName: "plus" | "edit";
        onPress?: (keyLabel: string, keyCode: string | number) => void
    }
) {
    const [keyLabel, setKeyLabel] = useState(item[0]);
    const [keyCode, setKeyCode] = useState(item[1].toString());
    const [editable, setEditable] = useState(false);
    useEffect(() => {
        setKeyLabel(item[0]);
        setKeyCode(item[1].toString());
    }, [item]);
    return (
        <View style={{ flexDirection: "row", gap: 8, margin: 4 }}>
            <TextInput
                editable={editable || iconName === "plus"}
                style={{ borderWidth: 1, padding: 4, minWidth: 40 }}
                value={keyLabel}
                onChangeText={(txt) => setKeyLabel(txt)}
            />
            <TextInput
                editable={editable || iconName === "plus"}
                inputMode="numeric"
                style={{ borderWidth: 1, padding: 4, minWidth: 40 }}
                value={keyCode}
                onChangeText={(txt) => setKeyCode(txt)}
            />
            <AntDesign.Button
                backgroundColor="transparent"
                borderRadius={0}
                color="black"
                iconStyle={{ marginRight: 0 }}
                name={iconName}
                onPress={() => {
                    onPress?.(keyLabel, keyCode);
                    setEditable((prevState) => !prevState);
                }}
                size={24}
                style={{ borderColor: "black", borderWidth: 1 }}
            />
        </View>
    );
}

export { KeyRow };
