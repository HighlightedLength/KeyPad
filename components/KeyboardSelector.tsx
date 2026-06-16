import { KeyboardLayoutSelectors } from '@/data/keyboardLayouts';
import {Picker} from '@react-native-picker/picker';
import { useState } from 'react';
import { useSelector } from 'react-redux';

function KeyboardSelector({ selectedValue, onValueChange }: { selectedValue: string; onValueChange: (itemValue: string) => void }) {
    const keyboardLayoutNames = useSelector(KeyboardLayoutSelectors.selectKeyboardNames);
    return (
        <Picker
        selectedValue={selectedValue}
        onValueChange={onValueChange}>
            {keyboardLayoutNames.map((layoutName) => (
                <Picker.Item label={layoutName} value={layoutName} key={layoutName} />
            ))}
        </Picker>
    );
}

export { KeyboardSelector };
