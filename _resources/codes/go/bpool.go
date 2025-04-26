package main

type BytePoolCap struct {
	c    chan []byte // 字节缓存池
	w    int         // []byte 的 len
	wcap int         // []byte 的 cap
}

// Get 从字节池中获取一个 []byte
func (bp *BytePoolCap) Get() (b []byte) {
	select {
	case b = <-bp.c: // 直接返回存在的字节数组
	default:
		if bp.wcap > 0 {
			b = make([]byte, bp.w, bp.wcap)
		} else {
			b = make([]byte, bp.w)
		}
	}
	return
}

func (bp *BytePoolCap) Put(b []byte) {
	select {
	case bp.c <- b: // 将 []byte 存入字节池
	default: // 丢弃 []byte
	}
}

func NewBytePoolCap(maxSize int, width int, widthCap int) *BytePoolCap {
	return &BytePoolCap{
		make(chan []byte, maxSize),
		width,
		widthCap,
	}
}
