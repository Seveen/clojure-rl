(defproject warlock-rl "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/core.async "0.6.532"]
                 [org.hexworks.zircon/zircon.core-jvm  "2019.1.2-PREVIEW"]
                 [org.hexworks.zircon/zircon.jvm.swing "2019.1.2-PREVIEW"]]
  :repositories [["jitpack" "https://jitpack.io"]]
  :repl-options {:init-ns warlock-rl.core})
