import { registerWebModule, NativeModule } from 'expo';
import { ExpoBleModuleEvents } from './ExpoBle.types';

class ExpoBleModule extends NativeModule<ExpoBleModuleEvents> {
  PI = Math.PI;
  hello() {
    return 'Hello world! 👋';
  }
};

export default registerWebModule(ExpoBleModule, 'ExpoBleModule');
