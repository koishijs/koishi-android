import { registerPlugin } from '@capacitor/core'

export interface NativeInterface {

}

const native = registerPlugin<NativeInterface>('native')

export { native }
