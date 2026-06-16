import ExpoBleModule from "./src/ExpoBleModule";

export function getTheme(): string {
  return ExpoBleModule.getTheme();
}

export function setTheme(theme: string): void {
  return ExpoBleModule.setTheme(theme);
}
