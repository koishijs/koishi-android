import { registerPlugin } from '@capacitor/core'

export interface Instance {
  name: string
  link: string
  status: 'Running' | 'Stopped'
}

export interface NativeInterface {
  starting(): Promise<void>
  instances(): Promise<{ value: Instance[] }>
  startInstance(value: { name: string }): Promise<{ value: boolean }>
  stopInstance(value: { name: string }): Promise<{ value: boolean }>
  createInstance(value: { name: string }): Promise<{ value: boolean }>
  removeInstance(value: { name: string }): Promise<{ value: boolean }>
  getPreferenceString(value: { key: string, default: string }): Promise<string>
  getPreferenceBoolean(value: { key: string, default: boolean }): Promise<boolean>
  setPreferenceString(value: { key: string, value: string }): Promise<void>
  setPreferenceBoolean(value: { key: string, value: boolean }): Promise<void>
  toast(value: { value: string }): Promise<void>
}

const native = registerPlugin<NativeInterface>('native')

export { native }
