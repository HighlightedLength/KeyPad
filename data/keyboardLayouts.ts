import { createSelector, createSlice, PayloadAction } from '@reduxjs/toolkit';
import type { KeyboardLayoutType, KeyMap } from '@/assets/keyboardLayouts/types';

import { RootState } from './store';

export interface CustomKeyboardLayoutType extends KeyboardLayoutType {
    type: 'custom',
}

export interface KeyboardLayoutState {
    layoutList: CustomKeyboardLayoutType[],
}

export type AddKeyActionPayload = {
    id: string;
    keyLabel: string;
    keyCode: number;
}

const initialState: KeyboardLayoutState = {
    layoutList: [
        {
            id: 'custom1',
            type: 'custom',
            keys: new Map<string, number>([['A', 65], ['B', 66], ['C', 67]]),
        }
    ],
};

const keyboardLayoutSlice = createSlice({
    name: 'keyboardLayout',
    initialState,
    reducers: {
        addKey(state, action: PayloadAction<AddKeyActionPayload>) {
            const { id, keyLabel, keyCode } = action.payload;
            const layout = state.layoutList?.find((layout) => layout.id === id);
            if (layout) {
                layout.keys.set(keyLabel, keyCode);
            }
        }
    },
});

const selectLayoutList = (state: RootState): CustomKeyboardLayoutType[] => state.keyboardLayout.layoutList;

export const KeyboardLayoutSelectors = {
    selectLayoutList,
    selectKeyboardNames: createSelector(
        [selectLayoutList],
        (layoutList: CustomKeyboardLayoutType[]): string[] => layoutList.map((layout) => layout.id),
    ),
};

// export const { setLayout } = keyboardLayoutSlice.actions;
export default keyboardLayoutSlice.reducer;