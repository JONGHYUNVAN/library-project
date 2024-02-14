'use client'
import {useRouter} from "next/navigation";

export default function Home() {
  let name = 'J.H.VAN';
  const router = useRouter();
  return (
      <div className="main-page"
           onClick={() => router.push('/ranking')}>
        <div className="title"style={{ position: 'relative', backgroundSize: 'cover', backgroundPosition: 'center' }}>
          <h4>library project</h4>
          <span className="title-sub">by {name}</span>
        </div>
        <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Long+Cang&display=swap"
        />
        <video className="background-video" autoPlay muted loop>
          <source src="/background.mp4" type="video/mp4" />
        </video>
      </div>
  );
}

