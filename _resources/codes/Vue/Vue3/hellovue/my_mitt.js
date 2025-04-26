function mitt(all) {
    all = all || new Map()
    return {
        all,
        on(type, handler) {
            const handlers = all[type]
            const added = handlers && handlers.push(handler)
            if (!added) {
                all[type] = [handler]
            }
        },
        off(type, handler) {
            const handlers = all[type]
            if (handlers) {
                handlers.splice(handlers.indexOf(handler) >>> 0, 1)
            }
        },
        emit(type, evt) {
            (all[type] || []).slice().map(handler => handler(evt))
            (all['*'] || []).slice().map(handler => handler(type, evt))
        },
    }
}