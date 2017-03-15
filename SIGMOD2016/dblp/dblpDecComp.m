set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [444.7041892	383.4785152	361.6826972	325.6193501	302.3997079];
y2 = [34.83079042	59.52149179	58.01696374	63.78677478	53.18961362];
y3 = [51.56	33.42	28.24	19.09	15.89];

p2 = plot(x, y2, '-ks');
hold on;
p3 = plot(x, y3, '-kd');

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 200]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
leg=legend('Local','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);