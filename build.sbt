import org.scalajs.linker.interface.ModuleSplitStyle

val scalaJsReact = "2.1.1"

/* ScalablyTyped configuration */
enablePlugins(ScalablyTypedConverterGenSourcePlugin)

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  Seq(
    version                       := "nkgm_main",
    homepage                      := Some(url("https://github.com/cquiroz/scalajs-floating-ui")),
    Global / onChangedBuildSource := ReloadOnSourceChanges
  ) ++ lucumaPublishSettings
)

addCommandAlias(
  "restartWDS",
  "; demo / Compile / fastOptJS / stopWebpackDevServer; demo / Compile /fastOptJS / startWebpackDevServer"
)

lazy val facade = project
  .in(file("facade"))
  .settings(name := "scalajs-react-floatingui")
  .settings(
    crossScalaVersions      := Seq("2.13.7", "3.1.0"),
    libraryDependencies ++= Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % scalaJsReact
    ),
    // shade into another package
    stOutputPackage         := "floatingui",
    /* javascript / typescript deps */
    Compile / npmDependencies ++= Seq(
      "@floating-ui/react-dom" -> "0.7.2"
    ),
    /* disabled because it somehow triggers many warnings */
    scalaJSLinkerConfig ~= (_.withSourceMap(false)),
    // because npm is slow
    useYarn                 := true,
    stSourceGenMode         := SourceGenMode.ResourceGenerator,
    stUseScalaJsDom         := true,
    scalacOptions ~= (_.filterNot(
      Set(
        // By necessity facades will have unused params
        "-Wdead-code",
        "-Wunused:params",
        "-Wunused:imports",
        "-Wunused:explicits"
      )
    )),
    Compile / doc / sources := Seq()
    // focus only on these libraries
    // stMinimize              := Selection.AllExcept("@svgdotjs/svg.js")
    // stMinimize := Selection.All,
    // stMinimizeKeep ++= List("svgdotjsSvgJs.mod.Element")
  )
  .settings(lucumaScalaJsSettings: _*)
  .enablePlugins(ScalablyTypedConverterGenSourcePlugin)

lazy val demo =
  project
    .in(file("demo"))
    .enablePlugins(ScalaJSPlugin)
    .settings(
      test            := {},
      libraryDependencies ++= Seq(
        "com.github.japgolly.scalajs-react" %%% "core" % scalaJsReact
      ),
      Compile / fastLinkJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) },
      Compile / fullLinkJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) },
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
      Compile / fastLinkJS / scalaJSLinkerConfig ~= (_.withModuleSplitStyle(
        ModuleSplitStyle.SmallestModules
      )),
      Compile / fullLinkJS / scalaJSLinkerConfig ~= (_.withModuleSplitStyle(
        ModuleSplitStyle.FewestModules
      )),
      publish / skip  := true,
      publish         := {},
      publishLocal    := {},
      publishArtifact := false,
      Keys.`package`  := file("")
    )
    .dependsOn(facade)
