import React from 'react';
import ReactDOM from 'react-dom';
import { IconlyProvider } from 'react-iconly';
import { BrowserRouter } from 'react-router-dom';

import { AppRoutes } from '@/Routes';

import './index.css';

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <IconlyProvider set="bulk" primaryColor="blue" secondaryColor="blue" stroke="bold" size="xlarge">
        <AppRoutes />
      </IconlyProvider>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById('root')
);
