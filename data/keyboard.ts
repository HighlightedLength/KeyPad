import { createSlice, createSelector } from '@reduxjs/toolkit'
// import type { PayloadAction } from '@reduxjs/toolkit'
import { ViewStyle } from 'react-native'

import { layouts, type LayoutName } from '@/assets/keyboardLayouts';
import { KeyboardLayoutType } from '@/assets/keyboardLayouts/types';

export interface KeyboardState {
  style: ViewStyle
  layout: LayoutName
}

const initialState: KeyboardState = {
  style: {
    flex: 1, 
    flexWrap: 'wrap', 
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    gap: 8,
    maxHeight: '50%',
    margin: 4,
  },
  layout: 'alphabetical',
}

export const keyboardSlice = createSlice({
  name: 'keyboard',
  initialState,
  reducers: {
    setKeyboardLayout(state, action: { payload: LayoutName }) {
      state.layout = action.payload;
    },
  },
});

const selectLayoutName = (state: { keyboard: KeyboardState }) => state.keyboard.layout;
export const getLayout = (name: LayoutName): KeyboardLayoutType['keys'] => layouts[name].keys;

export const KeyboardStateSelectors = {
  selectKeyboardLayout: createSelector(
    [selectLayoutName],
    (selectedLayoutName) => getLayout(selectedLayoutName),
  ),
}

// // Action creators are generated for each case reducer function
export const { setKeyboardLayout } = keyboardSlice.actions

export default keyboardSlice.reducer