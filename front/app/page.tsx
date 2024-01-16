// @ts-ignore
import Image from 'next/image'

export default function Home() {
  let name = 'J.H.VAN';
  let link = 'http://google.com';
  return (
      <div className="main-page">
        <div className="title"style={{ position: 'relative', background: 'url("/navbar.mp4")', backgroundSize: 'cover', backgroundPosition: 'center' }}>
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
