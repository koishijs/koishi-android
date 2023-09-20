import { registerPlugin } from '@capacitor/core'

export interface NativeInterface {
  starting(): Promise<void>
}

const native = registerPlugin<NativeInterface>('native')

export { native }
