import { memo } from "react";
import { Text, View } from "react-native";
import { predefinedLayouts } from "@/assets/keyboardLayouts";
import { setKeyboardLayout } from "@/data/keyboard";
import { useDispatch } from "react-redux";

// const staticLayouts = ["alphabetical", "qwerty", "dvorak", "azerty", "colemak"] as const;

const MemoizedPredefinedLayouts = memo(function PredefinedLayouts() {
    const dispatch = useDispatch();
    return (
        <>
            {
                predefinedLayouts.map(
                    (layoutName) => (
                        <Text key={layoutName} onPress={() => dispatch(setKeyboardLayout(layoutName))}>
                            {layoutName}
                        </Text>
                    )
                )
            }
        </>
    );
})

function SelectKeyboard() {
  return (
    <View style={{ gap: 24, margin: 24 }}>
        <MemoizedPredefinedLayouts />
    </View>
  );
}

export default SelectKeyboard;