(ns warlock-rl.view.zircon
  (:require [clojure.walk :refer
             :all])
  (:import (org.hexworks.zircon.api.mvc.base BaseView)
           (org.hexworks.zircon.api Components ComponentDecorations Positions TileColors Tiles)
           (org.hexworks.zircon.api.component.renderer ComponentDecorationRenderer)
           (org.hexworks.zircon.api.component ComponentAlignment)
           (org.hexworks.zircon.api.graphics BoxType)
           (org.hexworks.zircon.api.color TileColor)
           (org.hexworks.zircon.internal.component.renderer NoOpComponentRenderer)))

(def components
  {:button '(Components/button)
   :panel  '(Components/panel)})

(def verbs
  {:size     '.withSize
   :position '.withPosition
   :text     '.withText
   :decorations '.withDecorations
   :alignmentWithin '.withAlignmentWithin
   :renderer '.withComponentRenderer})

(def renderers
  {:no-op `(NoOpComponentRenderer.)})

(def alignments
  {:center 'ComponentAlignment/CENTER
   :left   'ComponentAlignment/LEFT})

(def decorations
  {:box `ComponentDecorations/box
   :shadow `ComponentDecorations/shadow
   :border `ComponentDecorations/border
   :halfblock `ComponentDecorations/halfBlock
   :side `ComponentDecorations/side})

(def decorations-defaults
  {:box {:type `BoxType/SINGLE :title ""}
   :side {:left \[ :right \]}})

(defn make-view
  [root]
  (proxy [BaseView] []
    (onDock []
      (.addComponent (.getScreen this) root))))

(defn format-decoration-parameters
  [params]
  (loop [result [] params params]
    (if params
      (let [[verb-tag param] (first params)
            verb (decorations verb-tag)
            defaults (decorations-defaults verb-tag)
            object `(~verb ~@(vals (merge defaults param)))
            threaded (conj result `(eval ~object))]
        (recur threaded (next params)))
      result)))

(defn format-params
  [verb params]
  (case verb
    :alignmentWithin (seq (replace alignments params))
    :decorations `((into-array ComponentDecorationRenderer ~(format-decoration-parameters params)))
    :renderer (seq (replace renderers params))
    (seq params)))

(defmacro defcomponent
  [name component args]
  (loop [x (components component), builders (seq args)]
    (if builders
      (let [[verb-tag params] (first builders)
            verb (verbs verb-tag)
            formatted-params (format-params verb-tag params)
            threaded `(~verb ~x ~@formatted-params)]
        (recur threaded (next builders)))
      `(def ~name (.build ~x)))))

(macroexpand-1 '(defcomponent panel :panel {:size            [40 10]
                                          :position    [1 1]
                                          :decorations {:box {:title "test"} :shadow {}}}))
(macroexpand-all '(defcomponent panel :panel {:size          [40 10]
                                          :position    [1 1]
                                          :decorations {:box {:title "test"} :shadow {}}}))

(defcomponent panel :panel {:size        [40 10]
                          :position    [1 1]
                          :decorations {:box {:title "test"} :shadow {}}})

(defn set-tile-at! [target x y tile]
  (.setTileAt target (Positions/create x y) tile))

(defn create-tile [fg bg character]
  (-> (Tiles/newBuilder)
      (.withForegroundColor (TileColors/fromString fg))
      (.withBackgroundColor (TileColors/fromString bg))
      (.withCharacter character)
      (.buildCharacterTile)))

(defn paint-world [panel world]
  (for [location world
        :let [[x y] (:position location)
              tile (:tile location)]]
    (set-tile-at! panel x y (create-tile (:fg-color tile) (:bg-color tile) (:char tile)))))
