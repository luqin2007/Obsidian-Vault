from manim import Scene, Square, FadeIn, Rotate, PI, FadeOut


class SomeAnimation(Scene):
    def construct(self):
        square = Square()
        self.play(FadeIn(square))
        self.play(Rotate(square, PI / 4))
        self.play(FadeOut(square))
        self.wait(1)
