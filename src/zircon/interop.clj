(ns zircon.interop
  (:import (kotlin.jvm.functions Function1 Function2)
           (org.hexworks.zircon.api.data Size3D Size Position Position3D)
           (kotlin Pair)))

(defn fn->fn1
  [function]
  (reify Function1
    (invoke [_ p1]
      (function p1))))

(defn fn->fn2
  [function]
  (reify Function2
    (invoke [_ p1 p2]
      (function p1 p2))))

(defn vec->size3D
  [[x y z]]
  (Size3D/create x y z))

(defn vec->size
  [[x y]]
  (Size/create x y))

(defn vec->pos3D
  [[x y z]]
  (Position3D/create x y z))

(defn vec->pos
  [[x y]]
  (Position/create x y))

(defn pos->vec
  [pos]
  [(.component1 pos) (.component2 pos)])

(defn vec->kotlin-pair [[k v]]
  (Pair. k v))