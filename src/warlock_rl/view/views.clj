(ns warlock-rl.view.views
  (:require [warlock-rl.view.zircon :as z]
            [clojure.walk :refer :all])
  (:import (org.hexworks.zircon.api Components ComponentDecorations Positions Tiles)
           (org.hexworks.zircon.api.component.renderer ComponentDecorationRenderer)
           (org.hexworks.zircon.api.component ComponentAlignment)
           (org.hexworks.zircon.api.graphics BoxType)))

(z/defcomponent
  game-area
  :panel
  {:size        [80 50]
   :position    [0 0]
   :decorations {:box {}}
   :renderer    [:no-op]})

(def start-view (z/make-view game-area))

(def change-world
  (partial z/paint-world game-area))

(def world
  [{:position [40 10] :tile {:fg-color "#000000" :bg-color "#ffff00" :char \t}}
   {:position [42 10] :tile {:fg-color "#000000" :bg-color "#ffffff" :char \O}}
   {:position [43 10] :tile {:fg-color "#000000" :bg-color "#ffffff" :char \n}}])

(z/paint-world game-area world)