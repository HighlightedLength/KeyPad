import { NativeModule, requireNativeModule } from 'expo';

import { ExpoBleModuleEvents } from './ExpoBle.types';

declare class ExpoBleModule extends NativeModule<ExpoBleModuleEvents> {
  setTheme: (theme: string) => void;
  getTheme: () => string;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<ExpoBleModule>('ExpoBle');
