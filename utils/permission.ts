import {requestMultiple, PERMISSIONS} from 'react-native-permissions';

async function requestBluetoothPermissions() {
    const permissionsResults = await requestMultiple([
        PERMISSIONS.ANDROID.BLUETOOTH_CONNECT,
        PERMISSIONS.ANDROID.BLUETOOTH_SCAN,
        PERMISSIONS.ANDROID.BLUETOOTH_ADVERTISE,
    ]);
    console.log('results: ', permissionsResults);
}

export {requestBluetoothPermissions};
