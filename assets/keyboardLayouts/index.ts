import { layout as alphabeticalLayout, layout } from './alphabetical';
import { layout as qwertyLayout } from './qwerty';
import { KeyboardLayoutType } from './types';

const alphabeticalLayoutMap = new Map<string, number>(alphabeticalLayout);
const qwertyLayoutMap = new Map<string, number>(qwertyLayout);

export const layouts: {
    [key: string]: KeyboardLayoutType;
} = {
    alphabetical: {
        keys: alphabeticalLayoutMap,
        type: 'predefined',
        id: 'alphabetical',
    },
    qwerty: {
        keys: qwertyLayoutMap,
        type: 'predefined',
        id: 'qwerty',
    },
};

export const predefinedLayouts = Object.keys(layouts);

export type LayoutName = keyof typeof layouts;