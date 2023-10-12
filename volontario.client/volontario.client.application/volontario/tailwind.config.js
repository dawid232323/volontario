/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        textPrimary: '#A1A1A1',
        bgPrimary: 'rgba(9,116,140,0.7)',
        bgPrimaryLight: 'rgba(9,116,140,0.7)',
        bgWhite: '#f2f2f2',
        btnAccentPrimary: '#FBC740',
        btnAccentHover: '#f6c033',
        rowAccentHover: 'rgba(251,199,64,0.2)',
        btnAccentHoverPrimary: 'rgba(11,143,171,0.7)',
        btnAccentHoverWhite: 'rgba(215,215,215,0.7)',
        btnAccentDisabled: 'rgba(175,172,172,0.77)',
        warnColor: 'rgba(255,71,71,0.77)',
        btnWarnHover: 'rgba(182,12,12,0.77)'
      },
      animation: {
        'bounce-slow': 'bounce 2s linear infinite'
      }
    },
  },
  plugins: [],
};
