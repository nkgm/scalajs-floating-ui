
import { DemoMain } from "@sjs/main.js";
DemoMain.main();

if (import.meta.hot) {
  import.meta.hot.accept();
}
