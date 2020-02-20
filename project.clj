(defproject warlock-rl "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/core.async "0.6.532"]
                 [org.hexworks.zircon/zircon.core-jvm  "2020.0.1-PREVIEW"]
                 [org.hexworks.zircon/zircon.jvm.swing "2020.0.1-PREVIEW"]
                 [org.hexworks.zircon/zircon.jvm.libgdx "2020.0.1-PREVIEW"]
                 [com.rpl/specter "1.1.3"]
                 [danlentz/clj-uuid "0.1.9"]
                 [vlaaad/reveal "0.1.0-ea1"]]
  :repositories [["jitpack" "https://jitpack.io"]
                 ["jcenter" "https://jcenter.bintray.com/"]]
  :repl-options {:init-ns          warlock-rl.core
                 :nrepl-middleware [vlaaad.reveal.nrepl/middleware]})
