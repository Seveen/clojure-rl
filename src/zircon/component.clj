(ns zircon.component
  (:require [zircon.interop :as i])
  (:import (org.hexworks.zircon.internal.component.renderer NoOpComponentRenderer)
           (org.hexworks.zircon.api ComponentDecorations GameComponents Components)
           (org.hexworks.zircon.api.graphics BoxType)
           (org.hexworks.zircon.api.component.renderer ComponentDecorationRenderer)
           (org.hexworks.zircon.api.game ProjectionMode)
           (org.hexworks.zircon.api.component ComponentAlignment)
           (org.hexworks.zircon.api.uievent MouseEventType KeyboardEventType)
           (org.hexworks.zircon.api.data Position)))

(def components
  {:button         `(Components/button)
   :panel          `(Components/panel)
   :label          `(Components/label)
   :game-component `(GameComponents/newGameComponentBuilder)})

(def verbs
  {:size              '.withSize
   :position          '.withPosition
   :text              '.withText
   :decorations       '.withDecorations
   :alignment-within  '.withAlignmentWithin
   :alignment-around  '.withAlignmentAround
   :renderer          '.withComponentRenderer
   :projection        '.withProjectionMode
   :game-area         '.withGameArea
   :tileset           '.withTileset})

(def renderers
  {:no-op `(NoOpComponentRenderer.)})

(def alignments
  {:center        `ComponentAlignment/CENTER
   :left-center   `ComponentAlignment/LEFT_CENTER
   :right-center  `ComponentAlignment/RIGHT_CENTER
   :top-left      `ComponentAlignment/TOP_LEFT
   :bottom-left   `ComponentAlignment/BOTTOM_LEFT
   :top-center    `ComponentAlignment/TOP_CENTER
   :bottom-center `ComponentAlignment/BOTTOM_CENTER
   :top-right     `ComponentAlignment/TOP_RIGHT
   :bottom-right  `ComponentAlignment/BOTTOM_RIGHT})

(def projections
  {:top-down `ProjectionMode/TOP_DOWN
   :oblique  `ProjectionMode/TOP_DOWN_OBLIQUE})

(def decorations
  {:box       `ComponentDecorations/box
   :shadow    `ComponentDecorations/shadow
   :border    `ComponentDecorations/border
   :halfblock `ComponentDecorations/halfBlock
   :side      `ComponentDecorations/side})

(def decorations-defaults
  {:box  {:type `BoxType/SINGLE :title ""}
   :side {:left \[ :right \]}})

(defn- format-decoration-parameters
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

(defn- format-params
  [verb params]
  (case verb
    :alignment-within (seq (replace alignments params))
    :alignment-around (seq (replace alignments params))
    :decorations      `((into-array ComponentDecorationRenderer
                                    ~(format-decoration-parameters params)))
    :renderer         (seq (replace renderers params))
    :projection       (seq (replace projections params))
    (seq params)))

(defmacro ->component [m]
  (let [type (:type m)
        args (dissoc m :type)]
    (loop [x (components type), builders (seq args)]
      (if builders
        (let [[verb-tag params] (first builders)
              verb (verbs verb-tag)
              formatted-params (format-params verb-tag params)
              threaded `(~verb ~x ~@formatted-params)]
          (recur threaded (next builders)))
        `(.build ~x)))))

(defn add-handler
  [component event handler]
  (let [fun (i/fn->handler handler)]
    (case event
      :mouse-pressed (.processMouseEvents component MouseEventType/MOUSE_PRESSED fun)
      :mouse-released (.processMouseEvents component MouseEventType/MOUSE_RELEASED fun)
      (.processKeyboardEvent component KeyboardEventType/KEY_PRESSED fun))))

(defn get-field [field component]
  (case field
    :text (.getText component)
    ()))

(defn set-field [field component value]
  (case field
    :text (.setText component value)
    ()))

(defn move-to [component [x y]]
  (.moveTo component (Position/create x y)))

(defn move-by [component [x y]]
  (.moveBy component (Position/create x y)))

(defn add-child [parent child]
  (.addComponent parent child))
