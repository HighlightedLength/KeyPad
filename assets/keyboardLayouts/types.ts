export type KeyPair = [string, number];

export type KeyMap = Map<string, number>;

export interface KeyboardLayoutType {
    keys: KeyMap;
    type: 'predefined' | 'custom',
    id: string,
}