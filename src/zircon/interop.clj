(ns zircon.interop
  (:import (org.hexworks.zircon.api Functions)
           (java.util.function BiConsumer)))

(defn fn->handler
  [function]
  (Functions/fromBiConsumer
    (reify BiConsumer
      (accept [_ t u]
        (function t u)))))
