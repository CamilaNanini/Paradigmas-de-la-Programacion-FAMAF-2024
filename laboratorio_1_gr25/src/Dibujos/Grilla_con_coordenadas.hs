module Dibujos.Grilla_con_coordenadas where

import Dibujo
import Dibujos.Grilla
import Graphics.Gloss
import Interp
import FloatingPic(Conf(..), Output, half, zero)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V
import Graphics.Gloss (Picture, black, color, line, pictures)
import Graphics.Gloss.Data.Vector (magV)

-- Les ponemos colorcitos para que no sea _tan_ feo
data ColorM = Negro
    deriving (Show, Eq)

data BasicaSinColor = Coord Int Int
    deriving (Show, Eq)

type Basica = (BasicaSinColor, ColorM)

colorear :: ColorM -> Picture -> Picture
colorear Negro = color black

gridHelper :: [Vector] -> Vector -> [[Vector]]
gridHelper [] y = []
gridHelper (x:xs) y = [x, x V.+ y] : (gridHelper xs y)

interpBasicaSinColor :: Output BasicaSinColor
interpBasicaSinColor (Coord i j) (x,y) w h = translate (10*wl/200 + x) (70*hl/200 + y) (scale (wl/300) (hl/300) (Text str))
    where
        -- TODO: No funcionará, pues vuelve absoluto el origen de la figura
        wl = (magV w)
        hl = (magV h)
        str = "("++ (show i) ++"," ++ (show j) ++ ")"
        
interpBas :: Output Basica
interpBas (b, c) x y w = colorear c $ interpBasicaSinColor b x y w

figNegra :: BasicaSinColor -> Dibujo Basica
figNegra b = figura (b, Negro)

gridRow :: Int -> Int -> [Dibujo (Basica)]
gridRow i limit = map (\x -> figNegra (Coord i x)) [0..(limit-1)]

grillaConf :: Conf
grillaConf = Conf {
    name = "Grilla"
    , pic = grilla (map (\x -> gridRow x 8) [0..7])
    , bas = interpBas
}

{- main :: IO ()
main = do
    initial grillaConf 400 -}