set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [4639.505301	4591.498119	4566.29648	4512.658696	4503.693034];
y2 = [1637.746846	2385.935088	3121.728037	3787.205012	4795.651274];
y3 = [294.15	192.47	184.41	189.26	179.52];

p2 = plot(x, y2, '-ks');
hold on;
p3 = plot(x, y3, '-kd');

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 8000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
leg=legend('Local','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);