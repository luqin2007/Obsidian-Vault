from manim import *


class Count(Animation):

    def __init__(self, num: DecimalNumber, count: float, **kwargs):
        # 一个 Mobject, 一个命名参数
        super().__init__(num, **kwargs)
        self.start = float(num.get_value())
        self.count = count

    def interpolate_mobject(self, alpha: float) -> None:
        value = self.start + alpha * self.count
        # 更新图形对象
        self.mobject.set_value(value)


class CountAnimation(Scene):
    def construct(self):
        number = DecimalNumber(10).set_color(WHITE).scale(5)
        self.add(number)
        self.wait()

        self.play(Count(number, 100), run_time=4, rate_func=linear)
        self.wait()
