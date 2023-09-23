import { registerPlugin } from '@capacitor/core'

export interface Instance {
  name: string
  status: 'Running' | 'Stopped'
}

export interface NativeInterface {
  starting(): Promise<void>
  instances(): Promise<{ value: Instance[] }>
  startInstance(value: { name: string }): Promise<{ value: boolean }>
  stopInstance(value: { name: string }): Promise<{ value: boolean }>
}

const native = registerPlugin<NativeInterface>('native')

export { native }
