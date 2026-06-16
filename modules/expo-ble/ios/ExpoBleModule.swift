import ExpoModulesCore

public class ExpoBleModule: Module {
  public func definition() -> ModuleDefinition {
    Name("ExpoBle")

    Function("getTheme") { () -> String in
      "system"
    }
  }
}
