(ns zircon.interop
  (:import (kotlin.jvm.functions Function1 Function2)))

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
