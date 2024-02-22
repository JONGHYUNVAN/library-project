'use client'
import { useEffect } from 'react';

export default function My() {
  return (
      <div>
          <div className="myInfo">

              <video className="background-video" autoPlay muted loop>
                  <source src="/backgroundMy.mp4" type="video/mp4" />
              </video>
          </div>
      </div>
  );
}

