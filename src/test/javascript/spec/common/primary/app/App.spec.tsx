import { render } from '@testing-library/react';
import App from '@/common/primary/app/App';

describe('App tests', () => {
  it('renders without crashing', () => {
    render(<App />);
  });
});
