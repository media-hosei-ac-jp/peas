import { Peas2Page } from './app.po';

describe('peas2 App', () => {
  let page: Peas2Page;

  beforeEach(() => {
    page = new Peas2Page();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
