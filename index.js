import {NativeModules} from 'react-native';

const {RNReactNativeMother} = NativeModules;

export function showSet() {
    RNReactNativeMother.showSet();
}

// Android only
export async function isInstalled(params) {
    return await RNReactNativeMother.isInstalled(params);
}

export async function isPush() {
    return await RNReactNativeMother.isPush()
}

export async function getIDFA() {
    return await RNReactNativeMother.getIDFA()
}

export async function getUUID() {
    return await RNReactNativeMother.getUUID()
}

export function showOtherIOS(params) {
    RNReactNativeMother.showOtherIOS(params)
}

export function iosShowStar() {
    RNReactNativeMother.iosShowStar()
}

export function iosShowComment(params) {
    RNReactNativeMother.iosShowComment(params)
}

export function iosShowDetail(params) {
    RNReactNativeMother.iosShowDetail(params)
}

export function androidScore(params) {
    RNReactNativeMother.androidScore(params)
}

export function showOtherAndroid(params) {
    RNReactNativeMother.showOtherAndroid(params)
}

export async function getStatusBarHeight() {
    return await RNReactNativeMother.getStatusBarHeight()
}

export async function getPermission(params) {
    return await RNReactNativeMother.getPermission(params)
}

export async function getPhoneInfo() {
    return await RNReactNativeMother.getPhoneInfo()
}

export async function getMetaDataFromActivity(params) {
    return await RNReactNativeMother.getMetaDataFromActivity(params)
}

export async function getMetaDataFromAppication(params) {
    return await RNReactNativeMother.getMetaDataFromAppication(params)
}

export async function getMetaDataIntFromAppication(params) {
    return await RNReactNativeMother.getMetaDataIntFromAppication(params)
}

export async function getMetaDataBooleanFromAppication(params) {
    return await RNReactNativeMother.getMetaDataBooleanFromAppication(params)
}