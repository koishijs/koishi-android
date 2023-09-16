import { inject } from 'vue'
import type { NativeInterface } from './register'

const native = inject<NativeInterface>('native')

export { native }