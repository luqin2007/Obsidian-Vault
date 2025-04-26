```python
from ctypes import windll


def is_admin() -> bool:
    """
    检查是否为管理员权限
    :return: 是否以管理员权限执行
    """
    try:
        return windll.shell32.IsUserAnAdmin()
    except:
        return False


def require_admin():
    """
    请求管理员权限
    :return: None
    """
    if not is_admin():
        windll.shell32.ShellExecuteW(None, 'runas', sys.executable, __file__, None, 1)
```
