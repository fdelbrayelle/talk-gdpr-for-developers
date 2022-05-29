import { Routes, Route, Link } from 'react-router-dom';

import App from '@/common/primary/app/App';
import { About } from '@/about/primary/app/About';

export const AppRoutes = () => (
  <Routes>
    <Route path="/" element={<App />} />
    <Route path="about" element={<About />} />
  </Routes>
);
