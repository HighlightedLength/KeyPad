import { useRouter } from 'expo-router';
import { useEffect } from 'react';
import { Button, Text, View } from "react-native";
import { useSelector } from "react-redux";

import Keyboard from "@/components/Keyboard";
import { selectPermissionResult } from '@/data/permission';
import { RootState } from "@/data/store";
import { KeyboardStateSelectors } from "@/data/keyboard";
import * as Ble from '@/modules/expo-ble';
import { requestBluetoothPermissions } from '@/utils/permission';

export default function Index() {
  const router = useRouter();
  const keyboardStyle = useSelector((state: RootState) => state.keyboard.style);
  const layout = useSelector(KeyboardStateSelectors.selectKeyboardLayout);
  const permissionResult = useSelector(selectPermissionResult);

  useEffect(() => {
    requestBluetoothPermissions();
  }, []);


  return (
    <View
      style={{
        flex: 1,
        justifyContent: "flex-end",
        alignItems: "center",
      }}
    >
      <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
        <Text>Theme: {Ble.getTheme()}</Text>
      </View>
      <View style={{ flexDirection: "row", gap: 24, marginBottom: 8 }}>
        <Button
          title="Select layout"
          onPress={() => {
            router.navigate("/selectKeyboard");
            console.log("Select layout pressed");
          }}
        />
        <Button
          title="Edit layout"
          onPress={() => {
            router.navigate("/editLayout");
            console.log("Edit layout pressed");
          }}
        />
      </View>
      <Text>Edit app/index.tsx to edit this screen 2.</Text>
      <Keyboard layout={layout} keyboardStyle={keyboardStyle} />
    </View>
  );
}
