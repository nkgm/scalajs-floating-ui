import reactRefresh from "@vitejs/plugin-react-refresh";
import path from "path";
import fs from "fs";

// https://vitejs.dev/config/
export default ({ command, mode }) => {
  const sjs =
    mode == "production"
      ? path.resolve(__dirname, "target/scala-2.13/demo-opt")
      : path.resolve(__dirname, "target/scala-2.13/demo-fastopt/");
  const webapp = path.resolve(__dirname, "src/main/webapp/");
  const themeConfig = path.resolve(webapp, "theme/theme.config");
  const themeSite = path.resolve(webapp, "theme/site");
  return {
    root: "src/main/webapp",
    resolve: {
      alias: [
        {
          find: "@sjs",
          replacement: sjs,
        },
      ],
    },
    server: {
      watch: {
        ignored: [
          function ignoreThisPath(_path) {
            const sjsIgnored =
              _path.includes("/target/stream") ||
              _path.includes("/zinc/") ||
              _path.includes("/classes");
            return sjsIgnored;
          },
        ],
      },
    },
    build: {
      // minify: 'esbuild',
      terserOptions: {
        sourceMap: false
      },
      outDir: path.resolve(__dirname, "../docs"),
    },
    plugins: [reactRefresh()],
  };
};
