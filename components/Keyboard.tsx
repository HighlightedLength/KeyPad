import { useMemo } from "react";
import { StyleProp, Text, View, ViewStyle } from "react-native";

import { KeyMap } from "@/assets/keyboardLayouts/types";
import { SingleKey } from "./SingleKey";

function Keyboard({ layout, keyboardStyle }: { layout?: KeyMap , keyboardStyle: StyleProp<ViewStyle> }) {
    const memoedKeys = useMemo(() => Array.from(layout?.entries() || []), [layout]);
    return (
        <View style={keyboardStyle}>
            {memoedKeys.map((keyPair) => {
                return (
                    <SingleKey keyLabel={keyPair[0]} keyCode={keyPair[1]} key={keyPair[0]}/>
                );
            })}
        </View>
    );
}

export default Keyboard;
