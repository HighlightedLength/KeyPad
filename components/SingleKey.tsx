import { Text, View } from "react-native";

function SingleKey(props: { keyLabel: string; keyCode: number }) {
    const { keyCode, keyLabel } = props;
    return (
        <View style={{ height: 60, width: 60, backgroundColor: 'grey', justifyContent: 'center', alignItems: 'center', borderRadius: 8 }}>
            <Text>{keyLabel}</Text>
        </View>
    );
}

export { SingleKey };
