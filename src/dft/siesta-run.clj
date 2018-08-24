(ns dft.siesta-run
  (:require
   [me.raynes.conch.low-level :as sh]
   [me.raynes.conch
    :refer [programs with-programs let-programs]]))


;; Low-level proc to run siesta within a working `directory` context
(defn siesta-run [executable numcores fdf directory
                  & {:keys [silent] :or {silent false}}]
  (let [siesta (sh/proc "mpirun" "-np" (.toString numcores)
                        executable :dir directory)]
    (when-not silent
      (future (sh/stream-to-out siesta :out)))
    (sh/feed-from-string siesta fdf)
    (sh/done siesta)
    (sh/exit-code siesta)))


;; grid2cube also needs a context to run
(defn g2c-util [syslabel directory]
  (let [g2c-ng (sh/proc "g2c_ng"
                        "-s" (str syslabel ".STRUCT_OUT")
                        "-g" (str syslabel ".EBS")
                        :dir directory)]
    (future (sh/stream-to-out g2c-ng :out))
    (sh/exit-code g2c-ng)))
