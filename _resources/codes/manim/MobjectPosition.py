from manim import *


class MobjectPosition(Scene):
    def construct(self):
        p1 = [-1, -1, 0]
        p2 = [1, -1, 0]
        p3 = [1, 1, 0]
        p4 = [-1, 1, 0]

        # 绘制线段
        a = Line(p1, p2).append_points(Line(p2, p3).points).append_points(Line(p3, p4).points)
        point_start = a.get_start()
        point_end = a.get_end()
        point_center = a.get_center()

        # 坐标显示
        self.add(Text(f"a.get_start() = {np.round(point_start, 2).tolist()}", font_size=24)
                 .to_edge(UR)
                 .set_color(YELLOW))
        self.add(Text(f"a.get_end() = {np.round(point_end, 2).tolist()}", font_size=24)
                 .next_to(self.mobjects[-1], DOWN)
                 .set_color(RED))
        self.add(Text(f"a.get_center() = {np.round(point_center, 2).tolist()}", font_size=24)
                 .next_to(self.mobjects[-1], DOWN)
                 .set_color(BLUE))

        # 绘制端点
        self.add(Dot(a.get_start()).set_color(YELLOW).scale(2))
        self.add(Dot(a.get_end()).set_color(RED).scale(2))
        self.add(Dot(a.get_top()).set_color(GREEN_A).scale(2))
        self.add(Dot(a.get_bottom()).set_color(GREEN_D).scale(2))
        self.add(Dot(a.get_center()).set_color(BLUE).scale(2))
        self.add(Dot(a.point_from_proportion(0.5)).set_color(ORANGE).scale(2))
        self.add(*[Dot(x) for x in a.points])
        self.add(a)

        self.wait(10)