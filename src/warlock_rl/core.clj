(ns warlock-rl.core
  (:require [clojure.core.async :as async :refer [go go-loop]]
            [warlock-rl.view.views :as v]
            [warlock-rl.view.zircon :as z])
  (:import (org.hexworks.zircon.api AppConfigs SwingApplications)
           (org.hexworks.zircon.internal.resource BuiltInCP437TilesetResource)))

(let [config (.build
               (doto (AppConfigs/newConfig)
                 (.withSize 80 50)
                 (.withDefaultTileset (BuiltInCP437TilesetResource/WANDERLUST_16X16))))
      app (SwingApplications/startApplication config)

      ]
  (.dock app v/start-view))

(def initial-world [{:position [40 10] :tile {:fg-color "#000000" :bg-color "#ffff00" :char \X}}
                    {:position [42 10] :tile {:fg-color "#000000" :bg-color "#ffffff" :char \s}}
                    {:position [43 10] :tile {:fg-color "#000000" :bg-color "#ffffff" :char \R}}])

(def ui {})

(defn process-ui [ui]
  ui)

(defn process-world [world]
  (v/paint-world world)
  world)

(v/paint-world initial-world)
(process-world initial-world)

;(go-loop [w initial-world ui ui]
;  (let [ui (process-ui ui)
;        world (process-world w ui)]
;    (v/paint-world world)
;    (recur world ui)))