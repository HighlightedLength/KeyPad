import { createSlice, createSelector } from '@reduxjs/toolkit'
// import type { PayloadAction } from '@reduxjs/toolkit'

export interface PermissionState {
  result: [];
}

const initialState: PermissionState = {
  result: [],
}

export const permissionSlice = createSlice({
  name: 'permission',
  initialState,
  reducers: {
    setPermissionResult(state, action: { payload: [] }) {
      state.result = action.payload;
    },
  },
});

export const selectPermissionResult = (state: { permission: PermissionState }) => state.permission.result;

export const { setPermissionResult } = permissionSlice.actions

export default permissionSlice.reducer