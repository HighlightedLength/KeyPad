import { configureStore } from '@reduxjs/toolkit'

import keyboardReducer from './keyboard';
import keyboardLayoutReducer from './keyboardLayouts';
import permissionReducer from './permission';

export const store = configureStore({
  reducer: {
    keyboard: keyboardReducer,
    keyboardLayout: keyboardLayoutReducer,
    permission: permissionReducer,
  },
});

// // Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>
// // Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch
