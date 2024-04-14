'use client'
import React, { useEffect } from 'react';
// @ts-ignore
import { particlesCursor } from 'threejs-toys';


export default function Cursor() {
    useEffect(() => {
        const pc = particlesCursor({
            el: document.getElementById('app'),
            gpgpuSize: 250,// 갯수
            color: 0xDAA520,// 입자색
            colors: [0xDAA520, 0x800000],//불꽃 색 범위
            coordScale: 1,//운동계수
            pointSize: 12,//크기
            noiseIntensity: 0.001,//노이즈 강도
            noiseTimeCoef: 0.0001,//노이즈 변화율
            pointDecay: 0.002,//소멸속도
            sleepRadiusX: 500, // sleep시 x각
            sleepRadiusY: 500,
            sleepTimeCoefX: 0.01,// sleep 시 함수계수
            sleepTimeCoefY: 0.01,
        });
        const h1 = document.querySelector('#app h1')as HTMLElement;

        const handleClick = () => {
            pc.uniforms.uColor.value.set(Math.random() * 0xffffff);
            pc.uniforms.uCoordScale.value = 0.005 + Math.random() * 2
            pc.uniforms.uNoiseIntensity.value = 0.0005 + Math.random() * 0.001
            pc.uniforms.uPointSize.value = 10 + Math.random() * 10
            const color = '#' + Math.floor(Math.random()*16777215).toString(16);
            const alpha1 = 1;
            const alpha2 = 0.7;
            const alpha3 = 0.5;

            h1.style.textShadow =
                `0 0 5px rgba(${parseInt(color.substring(1, 3), 16)}, ${parseInt(color.substring(3, 5), 16)}, ${parseInt(color.substring(5, 7), 16)}, ${alpha1}),
                 0 0 10px rgba(${parseInt(color.substring(1, 3), 16)}, ${parseInt(color.substring(3, 5), 16)}, ${parseInt(color.substring(5, 7), 16)}, ${alpha2}),
                 0 0 20px rgba(${parseInt(color.substring(1, 3), 16)}, ${parseInt(color.substring(3, 5), 16)}, ${parseInt(color.substring(5, 7), 16)}, ${alpha3})`;

        };

        document.body.addEventListener('click', handleClick);

        // 클린업 함수에서 이벤트 리스너 제거
        return () => {
            document.body.removeEventListener('click', handleClick);
        };

    }, []);

    return (
        <div id="app" >
            <h1>Library Project<br/>by J.H.Van</h1>
        </div>
    );
}