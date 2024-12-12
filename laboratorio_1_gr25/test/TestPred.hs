module Main (main) where
import qualified System.Exit as Exit
import Control.Monad (when)
import Pred

import Test.HUnit
import Dibujo

testCambiar :: Test 
testCambiar = TestList[
    TestCase (assertEqual "Error en testCambiar en el caso de figura" (figura 4) (cambiar (\x -> x == 3) (\x -> figura (x + 1)) (figura 3))),
    TestCase (assertEqual "Error en testCambiar en el caso de encimar" (encimar (figura 3) (figura 8)) ( cambiar (\x -> mod x 2 == 0) (\x -> figura (x*2)) (encimar (figura 3) (figura 4))) ),
    TestCase (assertEqual "Error en testCambiar en el caso de apilar" (apilar 1.0 1.0 (figura 5) (figura 5)) ( cambiar (\x -> mod x 2 == 0) (\x -> figura (x+1)) (apilar 1.0 1.0 (figura 4) (figura 5))) ),
    TestCase (assertEqual "Error en testCambiar en el caso de juntar" (juntar 1.0 1.0 (figura 14) (figura 21)) (cambiar (\x -> x < 5) (\x -> figura (x * 7)) (juntar 1.0 1.0 (figura 2) (figura 3)))),
    TestCase (assertEqual "Error en testCambiar en el caso de rot45" (rot45 (figura 3)) (cambiar (\x -> x == 5) (\x -> figura (x + 1)) (rot45 (figura 3)))),
    TestCase (assertEqual "Error en testCambiar en el caso de rotar" (rotar (figura 1)) (cambiar (\x -> x >= 5) (\x -> figura (x - 4)) (rotar (figura 5)))),
    TestCase (assertEqual "Error en testCambiar en el caso de espejar" (espejar (figura 4)) (cambiar (\x -> x == 3) (\x -> figura (x + 1)) (espejar (figura 3))))
    ]

testAny :: Test
testAny = TestList[
    TestCase (assertBool "Error en testAny en el caso de figura" (anyDib (\x -> x == 3) (figura 3)) ),
    TestCase (assertBool "Error en testAny en el caso de encimar" (anyDib (\x -> mod x 2 == 0) (encimar (figura 3) (figura 2))) ),
    TestCase (assertBool "Error en testAny en el caso de apilar" (anyDib (\x -> x > 3) (apilar 1.0 1.0 (figura 4) (figura 5))) ),
    TestCase (assertBool "Error en testAny en el caso de juntar" (anyDib (\x -> x <= 7) (juntar 1.0 1.0 (figura 7) (figura 3)))),
    TestCase (assertBool "Error en testAny en el caso de rot45" (anyDib (\x -> x == 5) (rot45 (figura 5))) ),
    TestCase (assertBool "Error en testAny en el caso de rotar" (anyDib (\x -> (x > 0) && (x < 10)) (rotar (figura 3))) ),
    TestCase (assertBool "Error en testAny en el caso de espejar" (anyDib (\x -> (x+4)==8) (espejar (figura 4))) )
    ]

testAll :: Test
testAll = TestList [
    TestCase (assertBool "Error en testAll en el caso de figura" ( allDib (=="Pablo") (Figura "Pablo") ) ),
    TestCase (assertBool "Error en TestaAll en el caso de encimar" (allDib (=="Pablo") (Encimar (Figura "Pablo") (Figura "Pablo")))),
    TestCase (assertBool "Error en TestaAll en el caso de encimar con pred negado" (allDib (/="Pablo") (Encimar (Figura "Favio") (Figura "Juan Roman Riquelme el último 10")))),
    TestCase (assertBool "Error en testAll en el caso de apilar" (allDib (\x -> (x > 20) || (x < 10)) (Apilar 1.0 1.0 (Figura 21) (Figura 9)))),
    TestCase (assertBool "Error en testAll en el caso de juntar" (allDib (\x -> x <= 865) (Juntar 1.0 1.0 (Figura 854) (Figura 546))) ),
    TestCase (assertBool "Error en testAll en el caso de rot45" (allDib (\x -> x > 2) (Rot45 (Figura 5))) ),
    TestCase (assertBool "Error en testAll en el caso de rotar" (allDib (\x -> (x > 8) && (x < 10)) (Rotar (Figura 9))) ),
    TestCase (assertBool "Error en testAll en el caso de espejar" (allDib (\x -> x == 65) (Espejar (Figura 65))) )
    ]

testAnd :: Test
testAnd = TestCase (assertBool "Error en testAnd" (andP (\_ -> True) (\_ -> True) ()) )

testOr :: Test
testOr = TestList [
    TestCase (assertBool "Error en testOr con dos True" (orP (\_ -> True) (\_ -> True) ()) ),
    TestCase (assertBool "Error en testOr con False y True" (orP (\_ -> False) (\_ -> True) ()) ),
    TestCase (assertBool "Error en testOr con True y False" (orP (\_ -> True) (\_ -> False) ()) )
    ]

tests :: Test
tests = TestList [
    TestLabel "testCambiar" testCambiar,
    TestLabel "testAny" testAny,
    TestLabel "testAll" testAll,
    TestLabel "testAnd" testAnd,
    TestLabel "testOr" testOr
    ]

main :: IO ()
main = do
    result <- runTestTT tests
    if failures result > 0 then Exit.exitFailure else Exit.exitSuccess
    