'use client'
import React, { useEffect } from 'react';
// @ts-ignore
import { particlesCursor } from 'threejs-toys';


export default function Cursor() {
    useEffect(() => {
        const pc = particlesCursor({
            el: document.getElementById('app'),
            gpgpuSize: 500,// 갯수
            color: 0xDAA520,// 입자색
            colors: [0xDAA520, 0x800000],//불꽃 색 범위
            coordScale: 0.7,//운동계수
            pointSize: 15,//크기
            noiseIntensity: 0.003,//노이즈 강도
            noiseTimeCoef: 0.01,//노이즈 변화율
            pointDecay: 0.005,//소멸속도
            sleepRadiusX: 25, // 커서와의 거리들
            sleepRadiusY: 25,
            sleepTimeCoefX: 0.001,// 움직일때 비활성
            sleepTimeCoefY: 0.002,
        });


    }, []);

    return (
        <div id="app" >
            <h1>Library Project<br/>by J.H.Van</h1>
        </div>
    );
}