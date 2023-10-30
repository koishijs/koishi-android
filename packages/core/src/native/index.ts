import { inject } from 'vue'
import type { NativeInterface } from './register'

const useNative = () => inject<NativeInterface>('native')!

export { useNative }